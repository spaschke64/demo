package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ucar.ma2.Array;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.IOException;
import java.util.List;

@RestController
public class CdfController {
    CdfService cdfService = new CdfService();
    @GetMapping("/")
    public String getRoot() {
        System.out.println("Root endpoint");
        return "endpoints are /get-info /get-data /get-vars and /get-image";
    }

    @GetMapping("/get-info")
    public String getInfo() {

        return cdfService.getInfo();
    }
//http://localhost:8080/get-data?time_index=879.78516&z_index=2.0
    @RequestMapping("/get-data")
    public JSONArray getData(@RequestParam double time_index, @RequestParam double z_index) throws IOException {
        System.out.println("time_index: " + time_index);
        System.out.println("z_index: " + z_index);
        JSONArray jsonArray = new JSONArray();
        Array results = cdfService.getData(time_index, z_index);
        //convert results to json
        // save x, y, concentration in a json object filtered_results
        // loop through results and save each object in a json array
        int count = 0;
        for (int i = 0; i < results.getSize(); i++) {
            count = i * 3;
            Array result = results.reduce();
            double result_x = result.getDouble(count);
            System.out.println("result_x: " + result_x);
            double result_y = result.getDouble(count+1);
            System.out.println("result_y: " + result_y);
            double result_concentration = result.getDouble(count+2);
            System.out.println("result_concentration: " + result_concentration);
            JSONObject jsonObject = cdfService.makeJsonObject( result_x, result_y, result_concentration);
            jsonArray.put(jsonObject);
        }
        return jsonArray;
    }
    @RequestMapping("/get-image")
    public String getImage(@RequestParam double time_index, @RequestParam double z_index) throws IOException {
        Array data = cdfService.getData(time_index, z_index);

        // create an image of the data
        //save image as a png file
        //return the image

        cdfService.createImage(data);
        return "This is only a test.  If this were a real image, you would be instructed where to go and what to do.";
    }

    @GetMapping("/get-vars")
    public String getVars() {
        NetcdfFile netcdfFile = null;
        //open the file in the project directory at com.example.demo.concentration.timeseries.nc
        try {
            netcdfFile = NetcdfFile.open("target/classes/static/concentration.timeseries.nc");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        //get the file details
        String info = netcdfFile.getVariables().toString();
        try{
            netcdfFile.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return info;
    }
}
