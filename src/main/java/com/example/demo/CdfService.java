package com.example.demo;

import ucar.ma2.Array;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import java.io.IOException;
import java.util.List;

public class CdfService {

    // getData
    String getData(double time_index,double z_index) throws IOException {        //open the file in the project directory at com.example.demo.concentration.timeseries.nc
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
                x,
                y,
                z,
                time,
                concentration
        });
// read data from the file.
        List<Array> results = netcdfFile.readArrays(varNames);
        //get x, y and concentration values and put them in json format
        for (Array result : results) {
            System.out.println(result);
        }

        try{
            netcdfFile.close();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return results.toString();
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

}
