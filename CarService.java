package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.app.entiry.Car;
import com.app.repository.CarRepository;

@Service
public class CarService {
	@Autowired
	private CarRepository cr;
	
	
	
	public List<Car> getAllCars(){
		return cr.findAll();
	}
	
	public List<Car> getCarsByCategory(String category) {
        return cr.findByCategory(category);
    }
	
	public Car getCar(Long id) {
		return cr.findById(id).orElseThrow();
	}
	
	public Car createCar(Car car) {
		//RestTemplate restTemplate = new RestTemplate();
	    //ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:7021/app/api/image/upload", imageFile, String.class);
	    //String responseBody = response.getBody();
	    
		return cr.save(car);
	}
	
	public Car updateCar(Long id,Car car,  MultipartFile imageFile) {
	    Car excar = cr.findById(id).orElseThrow();
	    excar.setCategory(car.getCategory() != null ? car.getCategory() : excar.getCategory());
	    excar.setModel(car.getModel() != null ? car.getModel() : excar.getModel());
	    excar.setYear(car.getYear() == 0.0 ? excar.getYear() : car.getYear());
	    excar.setPrice(car.getPrice() == 0.0 ? excar.getPrice() : car.getPrice());
	    excar.setDimensions(car.getDimensions()!=null? car.getDimensions():excar.getDimensions());
	    excar.setEngine(car.getEngine()!=null? car.getEngine(): excar.getEngine());
	    excar.setFuelType(car.getFuelType()!= null? car.getFuelType():excar.getFuelType());
	    excar.setMileage(car.getMileage()==0.0? excar.getMileage(): car.getMileage());
	    excar.setTopSpeed(car.getTopSpeed()==0.0? excar.getTopSpeed(): car.getTopSpeed());

	    if (imageFile != null) {
	        // Upload the image
	        RestTemplate restTemplate = new RestTemplate();
	        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:7021/upload", imageFile, String.class);

	        // Get the response
	        String responseBody = response.getBody();

	        // Set the image file name
	        excar.setImg(responseBody);
	    }

	    // Update the car
	    return cr.save(excar);
	}
	
	public void deleteCar(Long id) {
		cr.deleteById(id);
	}

}
