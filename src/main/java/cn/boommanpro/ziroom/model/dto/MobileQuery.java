package cn.boommanpro.ziroom.model.dto;


import cn.boommanpro.ziroom.common.annotation.HttpQueryString;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wangqm-b
 * @create 2018/7/3
 */
@Data
public class MobileQuery {
    @HttpQueryString(name = "city_code")
    @ApiModelProperty("城市Code")
    private String cityCode="110000";

    @ApiModelProperty("关键字")
    private String keywords;

    @ApiModelProperty("地铁Code")
    @HttpQueryString(name = "subway_code")
    private String subwayCode;

    private int sort=2;

    @ApiModelProperty("查询数量")
    private int size = 10;

    @ApiModelProperty("查询页数")
    private int page = 1;

    @ApiModelProperty("价格区间")
    private String price="4500,7000";

    public void setPrice(Integer minPrice,Integer maxPrice) {
        if (minPrice==null&&maxPrice==null){
            this.price=",";
            return;
        }
        if (maxPrice==null){
            this.price = minPrice+",";
            return;
        }
        if (minPrice==null){
            this.price = ","+maxPrice;
            return;
        }
        this.price = minPrice + "," + maxPrice;
    }
}
