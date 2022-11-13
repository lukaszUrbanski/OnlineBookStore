package pl.urbanskilukasz.onlineLibrary.order.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public enum OrderStatus {
     NEW{
          @Override
          public UpdateStatusResult updateStatus(OrderStatus status) {
               return switch (status) {
                    case PAID -> UpdateStatusResult.ok(PAID);
                    case CANCELED -> UpdateStatusResult.revoked(CANCELED);
                    case ABANDONED -> UpdateStatusResult.revoked(ABANDONED);
                    default -> super.updateStatus(status);
               };
          }
     },
     PAID{
          @Override
          public UpdateStatusResult updateStatus(OrderStatus status) {
               return switch (status){
                    case SHIPPED -> UpdateStatusResult.ok(SHIPPED);
                    case CANCELED -> throw new IllegalStateException("Cannot cancel paid order.");
                    default -> super.updateStatus(status);
               };
          }
     },
     CANCELED,
     ABANDONED,
     SHIPPED{
          @Override
          public UpdateStatusResult updateStatus(OrderStatus status) {
               if (status == CANCELED){
                    throw new IllegalStateException("Cannot cancel shipped order.");
               }
               return super.updateStatus(status);
          }
     };

     public static Optional<OrderStatus> parseString(String value) {
          return Arrays.stream(values())
                  .filter(it -> StringUtils.equalsIgnoreCase(it.name(), value))
                  .findFirst();

     }

     public UpdateStatusResult updateStatus (OrderStatus status) {
          throw new IllegalArgumentException("Unable to mark " + this.name() + " oder as " + status.name());
     }

}
