package com.flybook;

import com.flybook.dbaccess.AirplaneDbAccess;
import com.flybook.dbaccess.AirportDbAccess;
import com.flybook.dbaccess.ClientDbAccess;
import com.flybook.dbaccess.FlightDbAccess;
import com.flybook.dbaccess.ProfileDbAccess;
import com.flybook.dbaccess.ReservationDbAccess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@SpringBootApplication
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@EnableFeignClients(clients = {
		AirplaneDbAccess.class,
		AirportDbAccess.class,
		ClientDbAccess.class,
		FlightDbAccess.class,
		ProfileDbAccess.class,
		ReservationDbAccess.class
})
public class FlybookApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlybookApplication.class, args);
	}
}
