package io.github.emreblbl.springbatch.processor;

import io.github.emreblbl.springbatch.model.Address;
import io.github.emreblbl.springbatch.model.Customer;
import io.github.emreblbl.springbatch.model.CustomerInput;
import io.github.emreblbl.springbatch.model.Gender;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class CustomItemProcessor implements ItemProcessor<CustomerInput, Customer> {

    private static final String UNITED_STATES_COUNTRY = "United States";

    private static final String DATE_PATTERN = "yyyy-MM-dd";

    @Override
    public Customer process(final CustomerInput customerInput) throws Exception {
        // filtering american customers. Any foreign customer will be discarded
        if (!UNITED_STATES_COUNTRY.equals(customerInput.getCountry())) {
            return null;
        }

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        final Customer customer = new Customer();
        final Address address = new Address();

        customer.setId(UUID.fromString(customerInput.getId()));
        customer.setFirstName(customerInput.getFirstName());
        customer.setLastName(customerInput.getLastName());
        customer.setGender(Gender.entryOf(customerInput.getGender()));
        customer.setBirthday(LocalDate.parse(customerInput.getBirthday(), formatter));

        address.setStreet(customerInput.getAddress());
        address.setCountry(customerInput.getCountry());

        customer.setAddress(address);

        return customer;
    }
}
