package cn.boommanpro.ziroom.controller;


import cn.boommanpro.ziroom.common.CallResult;
import cn.boommanpro.ziroom.common.HttpQueryStringUtil;
import cn.boommanpro.ziroom.model.dto.MobileQuery;
import cn.boommanpro.ziroom.model.entity.RoomJSON;
import cn.boommanpro.ziroom.model.entity.RoomsBean;
import cn.boommanpro.ziroom.ocr.ZiRoomPriceGrab;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@ApiModel(description = "自如信息查询")
@RequestMapping("/ziRoom")
public class ZiRoomController {

    @GetMapping("/search")
    @ApiOperation(value = "手机查询接口", notes = "")
    public CallResult search(@ApiParam(name = "手机查询") MobileQuery mobileQuery,
                             @ApiParam(name = "包含关键字") String contains) throws IOException {

        List<RoomsBean> roomsBeanList = new ArrayList<>();
        int startPageNum = 1;
        String url = "http://phoenix.ziroom.com/v7/room/list.json";
        String urlData;
        List<RoomsBean> roomsBeans;

        contains = contains == null ? "2室1厅" : contains;
        String queryString;
        do {
            mobileQuery.setPage(startPageNum);
            queryString = HttpQueryStringUtil.objectToQueryString(mobileQuery);

            urlData = Jsoup.connect(url + queryString)
                    .ignoreContentType(true)
                    .get().text();
            RoomJSON roomJSON = JSON.parseObject(urlData, RoomJSON.class);
            roomsBeans = roomJSON.getData().getRooms();
            for (RoomsBean roomsBean : roomsBeans) {
                if (roomsBean.getName().contains(contains) && !roomsBean.getStatus().equals("ycz")) {
                    roomsBeanList.add(roomsBean);
                }
                roomsBean.setPrice(ZiRoomPriceGrab.extractPrice(roomsBean.getPriceList().get(0), roomsBean.getPriceList().get(1)));

            }
            startPageNum++;
        } while (roomsBeans.size() != 0);

        return CallResult.success(roomsBeanList);
    }
}