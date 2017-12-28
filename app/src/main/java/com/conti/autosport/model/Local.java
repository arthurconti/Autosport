package com.conti.autosport.model;

import android.graphics.Bitmap;

import com.conti.autosport.util.Point;

import java.util.ArrayList;
import java.util.List;

public class Local {

    private List<Point> points = new ArrayList<>();

    private Bitmap map;
    private ArrayList<Servico> services;

    public Bitmap getMap() {
        return map;
    }

    public void setMap(Bitmap bitmap) {
        this.map = bitmap;
    }

    public ArrayList<Servico> getServices() {
        return services;
    }

    public void setServices(ArrayList<Servico> services) {
        this.services = services;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints(){
        return points;
    }
}
