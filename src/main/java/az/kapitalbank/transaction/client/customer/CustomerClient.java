package az.kapitalbank.transaction.client.customer;

import az.kapitalbank.transaction.client.customer.model.BalanceChangeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-customer", url = "${application.client.customer.url}/api/v1/internal/customers")
public interface CustomerClient {

    @PostMapping("/{customerId}/balance/increase")
    void increaseBalance(@PathVariable Long customerId, @RequestBody BalanceChangeRequest request);

}
