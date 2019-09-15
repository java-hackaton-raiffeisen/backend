package hackaton.raiffeisen.backend.viewmodels;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(description = "Данные с формы создания новой услуги")
@JsonRootName(value = "newOrder")
public class OrderView {

    @ApiModelProperty(value = "Тип услуги", required = true, example = "Урок русского языка")
    @Getter
    @Setter
    private String type;

    @ApiModelProperty(value = "Стоимость услуги", required = true, example = "1000.0")
    @Getter
    @Setter
    private Double allPaymentValue;

}
