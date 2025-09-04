package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.entiry.Car;
import com.app.entiry.FileUploadResponse;
import com.app.service.CarService;
import com.app.service.ImageService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/cars")
public class CarController {
	@Autowired
	private CarService cs;
	
	@Value("${spring.servlet.multipart.location}")
	String imagePath;
	
	@Autowired
	private ImageService Is;
	
	@GetMapping
    public List<Car> getAllCars() {
        return cs.getAllCars();
    }
	
	@GetMapping("/{id}")
    public Car getCar(@PathVariable Long id) {
        return cs.getCar(id);
    }
	
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Car> createCar(HttpServletRequest request, 
	                                      @RequestParam(value = "file") MultipartFile file, 
	                                      @RequestParam(value = "Car") String carJson) {
	    try {
	        // Deserialize JSON string to Car object
	        ObjectMapper objectMapper = new ObjectMapper();
	        Car car = objectMapper.readValue(carJson, Car.class);

	        FileUploadResponse response = Is.uploadImage(imagePath, file);
	        car.setImg(response.getFileName());
	        Car savedCar = cs.createCar(car);
	        return ResponseEntity.ok(savedCar);
	    } catch (Exception e) {
	        // Log the exception for debugging
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
	@PutMapping(value="/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
	public Car updateCar(@PathVariable Long id, 
            HttpServletRequest request, 
            @RequestPart(value = "Data") Car car, 
            @RequestPart(value = "file") MultipartFile file, 
            @RequestBody(required = false) Car jsonCar) {
Car carToUse = (car != null) ? car : jsonCar;
return cs.updateCar(id, carToUse, file);
}
	
	@DeleteMapping("/{id}")
	public void deleteCar(@PathVariable Long id) {
		cs.deleteCar(id);
	}
	
	

}
