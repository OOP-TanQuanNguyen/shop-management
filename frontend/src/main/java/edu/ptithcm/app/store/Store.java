package edu.ptithcm.app.store;
import java.util.*;
import java.util.function.Consumer;


import edu.ptithcm.app.AppState;

public class Store {
    private static Store instance;

    private final AppState appState = new AppState();
 
    private final Map<String,Consumer<Object>> reducers = new HashMap<>();

    private final List<Consumer<AppState>> listeners = new java.util.ArrayList<>();

    private Store() {}

    public static Store getInstance() {
        if (instance == null) {
            instance = new Store();
        }
        return instance;
    }

    public void registerReducer(String action,Consumer<Object> reducer) {
        this.reducers.put(action, reducer);
    }

    public void dispatch(String action,Object payload){
        Consumer<Object> reducer = this.reducers.get(action);
        if (reducer != null) {
            reducer.accept(payload);
            this.notifyListeners();
        }else{
            System.out.println("No reducer found for action: " + action);
        }
    }


    public void subcribe(Consumer<AppState> listener){
        this.listeners.add(listener);
        listener.accept(this.appState);
    }


    public void notifyListeners(){
        for(Consumer<AppState> listener : this.listeners){
            listener.accept(this.appState);
        }
    }

    public AppState getAppState() {
        return this.appState;
    }
}
