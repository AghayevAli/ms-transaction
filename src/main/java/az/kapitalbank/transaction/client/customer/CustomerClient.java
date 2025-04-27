package az.kapitalbank.transaction.client.customer;

import az.kapitalbank.transaction.client.customer.model.BalanceChangeRequest;
import az.kapitalbank.transaction.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-customer", url = "${application.client.customer.url}/api/v1/internal/customers",
        configuration = FeignConfig.class)
public interface CustomerClient {

    @PostMapping("/{customerId}/balance/increase")
    void increaseBalance(@PathVariable Long customerId, @RequestBody BalanceChangeRequest request);

    @PostMapping("/{customerId}/balance/decrease")
    void decreaseBalance(@PathVariable Long customerId, @RequestBody BalanceChangeRequest request);

}
