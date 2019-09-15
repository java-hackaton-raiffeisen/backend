package hackaton.raiffeisen.backend.viewmodels;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;

@ApiModel(value = "Описание услуги")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonRootName(value = "orders")
public class OrdersView {

    @ApiModelProperty(value = "Номер услуги", required = true)
    @Getter
    @Setter
    private Long orderNumber;

    @ApiModelProperty(value = "Стоимость услуги с вычетом комиссии банка", required = true)
    @Getter
    @Setter
    private Double paymentValue;

    @ApiModelProperty(value = "Полная стоимость услуги", required = true)
    @Getter
    @Setter
    private Double allPaymentValue;

    @ApiModelProperty(value = "Произведена ли оплата", required = true)
    @Getter
    @Setter
    private boolean isOrderPaid;

    @ApiModelProperty(value = "Дата оказания услуги", required = true)
    @Getter
    @Setter
    private LocalDate orderCreationDate;

    @ApiModelProperty(value = "Дата оплаты", required = true)
    @Getter
    @Setter
    private LocalDate paymentDate;

}
