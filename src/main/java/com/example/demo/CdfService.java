package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import ucar.ma2.Array;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dt.GridDataset;
import ucar.nc2.dt.GridDatatype;
import ucar.nc2.geotiff.GeotiffWriter;

import java.io.IOException;
import java.util.List;

public class CdfService {

    // getData
    Array getData(double time_index,double z_index) throws IOException {        //open the file in the project directory at com.example.demo.concentration.timeseries.nc
        NetcdfFile netcdfFile = null;
        try {
            netcdfFile = NetcdfFile.open("target/classes/static/concentration.timeseries.nc");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        assert netcdfFile != null;
        Variable x = netcdfFile.findVariable("x");
        Variable y = netcdfFile.findVariable("y");
        Variable z = netcdfFile.findVariable("z");
        Variable time = netcdfFile.findVariable("time");
        Variable concentration = netcdfFile.findVariable("concentration");

        //List<Array> contents = netcdfFile.readArrays();
        List<Variable> varNames = List.of(new Variable[]{
                 time,z,y,x,concentration
        });
        // read data from the file. Gives all the x,y,z, time, concentration data in the file
        List<Array> results = netcdfFile.readArrays(varNames);
        //this might work better
        //        public double[] getJavaArrayDouble(String memberName);

        try{
            netcdfFile.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        // filter results by time and z, returning only x, y, and concentration
        Array filtered_results = filterResults(results, time_index, z_index);

        return filtered_results;
    }
    // make image


    // get info
    String getInfo() {
        NetcdfFile netcdfFile = null;
        //open the file in the project directory at com.example.demo.concentration.timeseries.nc
        try {
            netcdfFile = NetcdfFile.open("target/classes/static/concentration.timeseries.nc");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        //get the file details
        String info = netcdfFile.getDetailInfo();
        try{
            netcdfFile.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return info;
    }


// this is confusing because we are getting data for 5 variables.
// I think we only want to loop through the Concentration results.
    //what confuses me is the different lengths of the arrays. But the concentration
    //is time, z,y,x. In this case it is (time=8, z=1, y=27, x=36)
    // perhaps I am reading it incorrectly. I should treat it as a 4 dimensional array.
    // concentration[time][z][y][x]
   Array filterResults(List<Array> results, double time_index, double z_index) {
        ucar.ma2.Array filtered_results = new ucar.ma2.ArrayDouble.D1(3) ;
       // filter results
        int count = 0;
        int num_found = 0;
        int filtered_count = 0;
//this loop has an error.
        for (ucar.ma2.Array result : results) {
            System.out.println(results);
            double result_time = result.getDouble(count);
            System.out.println("result_time: " + result_time);
            double result_y = result.getDouble(count+1);
            System.out.println("result_y: " + result_y);
            double result_x = result.getDouble(count+2);
            System.out.println("result_x: " + result_x);
            double result_z = result.getDouble(count+3);
            System.out.println("result_z: " + result_z);
            double result_concentration = result.getDouble(count+4);
            System.out.println("result_concentration: " + result_concentration);
   //debug get all time and z         if (result_time == time_index && result_z == z_index) {
                System.out.println("Found it!");
                // save result_x, result_y, result_concentration in  ucar.ma2.Array  filtered_results
                filtered_results.setDouble(filtered_count, result_x);
                filtered_results.setDouble(filtered_count+1, result_y);
                filtered_results.setDouble(filtered_count+2, result_concentration);
                num_found += 3;
                filtered_count += 3;
   //         }


            count +=5;

        } // end for
        System.out.println("num_found: " + num_found);
        return filtered_results;
    }

    // make json object
    JSONObject makeJsonObject(double x, double y, double concentration) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("x", x);
        jsonObject.put("y", y);
        jsonObject.put("concentration", concentration);
        return jsonObject;
    }

    void createImage(Array data) {
        NetcdfFile netcdfFile = null;
        //open the file in the project directory at com.example.demo.concentration.timeseries.nc
        try {
            netcdfFile = NetcdfFile.open("target/classes/static/concentration.timeseries.nc");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        // create an image of the data
        GridDatatype gridType = null;
        GridDataset gridDataset = null;

        //GeotiffWriter geotiffWriter = new ucar.nc2.geotiff.GeotiffWriter(fileOut);
        //geotiffWriter.writeGrid(gridDataset,gridType, data,true);
        //save image as a png file
        //return the image
    }
}
