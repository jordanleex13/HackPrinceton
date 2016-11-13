package com.jordanleex13.hackprinceton.MachineLearning;

/**
 * Created by Jordan on 2016-11-12.
 */

public class EmotionalWant {

    private double sports;
    private double calm;
    private double exciting;
    private double popularity;

    public EmotionalWant(double sports_, double calm_, double exciting_, double popularity_){
        sports = sports_;
        calm = calm_;
        exciting = exciting_;
        popularity = popularity_;
    }

    public String[] getSearchTerms(){
        if(exciting>0.5 && popularity>0.5){
            String[] returnvalues = {"concert"};
            return (returnvalues);
        }
        else if(calm> 0.5){
            String[] returnvalues = {"theater"};
            return (returnvalues);
        }
        else if(sports>0.5 && exciting>0.5) {
            String[] returnvalues = {"sports"};
            return (returnvalues);
        }
        else if (calm> 0.5 && popularity>0.5){
            String[] returnvalues = {"classical", "theater"};
            return (returnvalues);
        }
        else if(sports > 0.5 && calm > 0.5){
            String[] returnvalues = {"golf"};
            return returnvalues;
        }

        else if(exciting> 0.5){
            String[] returnvalues = {"concert","sports"};
            return (returnvalues);
        }
        else if(sports> 0.5){
            String[] returnvalues = {"sports"};
            return (returnvalues);
        }
        else if(popularity> 0.5){
            String[] returnvalues = {"sports", "concert"};
            return (returnvalues);
        }
        // last chance case
        else if(sports > calm && sports > exciting && sports > popularity){
            String[] returnvalues = {"sports"};
            return (returnvalues);
        }
        else if(calm > sports && calm > exciting && calm > popularity){
            String[] returnvalues = {"sports"};
            return (returnvalues);
        }
        else if(exciting > calm && exciting > sports && exciting > popularity){
            String[] returnvalues = {"exciting"};
            return (returnvalues);
        }
        else {
            String[] returnvalues = {"sports","theater","classical","concert"};
            return (returnvalues);
        }
    }

}
