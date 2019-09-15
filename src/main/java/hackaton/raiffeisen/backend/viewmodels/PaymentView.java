package hackaton.raiffeisen.backend.viewmodels;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@ApiModel(value = "информация о платеже")
@JsonRootName(value = "payment")
public class PaymentView {

    @ApiModelProperty(value = "статус оплаты", required = true, example = "success")
    @Getter
    @Setter
    private String status;

    @ApiModelProperty(value = "id пользователя", required = true, example = "1")
    @Getter
    @Setter
    private Long userId;

    @ApiModelProperty(value = "id клиента", required = true, example = "1")
    @Getter
    @Setter
    private Long clientId;

    @ApiModelProperty(value = "id предоставленной услуги (номер услуги)", required = true, example = "1")
    @Getter
    @Setter
    private Long orderId;

    @ApiModelProperty(value = "Cумма платежа", required = true, example = "1")
    @Getter
    @Setter
    @Min(value = 0)
    private Double paymentValue;

}
