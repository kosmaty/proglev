package com.proglev;

import com.proglev.gui.ProgLevGui;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProglevApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProglevApplication.class, args);
//		ProgLevGui.main(args);
	}
}
