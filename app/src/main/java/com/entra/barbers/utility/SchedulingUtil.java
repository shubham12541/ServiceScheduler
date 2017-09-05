package com.entra.barbers.utility;

import android.util.Pair;

import com.entra.barbers.models.Booking;
import com.entra.barbers.models.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by I334104 on 9/5/2017.
 */

public class SchedulingUtil {

    public Pair<String, String> getTimeAlloted(ArrayList<Booking> bookings, String startTime, String endTime, int totalTime){
        int startHours = Integer.parseInt(startTime.substring(0,2));
        int startMinutes = Integer.parseInt(startTime.substring(2, 4));

        int endHours = Integer.parseInt(endTime.substring(0, 2));
        int endMinutes = Integer.parseInt(endTime.substring(2, 4));

        int reqHours = totalTime/60;
        int reqMinutes = totalTime%60;

        if(reqHours > (endHours - startHours) || ( reqHours == (endHours-startHours) && reqMinutes > (endMinutes - startMinutes) ) ){
            return null;
        }

        Booking tempBook = new Booking();
        tempBook.setEndTime("0000");
        tempBook.setStartTime("0800");

        bookings.add(tempBook);

        Booking tempBook2 = new Booking();
        tempBook2.setStartTime("2000");
        tempBook2.setEndTime("2359");

        bookings.add(tempBook2);

        Collections.sort(bookings, new Comparator<Booking>() {
            @Override
            public int compare(Booking booking, Booking t1) {
                if(!booking.getStartTime().equals(t1.getStartTime())){
                    return booking.getStartTime().compareTo(t1.getStartTime());
                } else{
                    return booking.getEndTime().compareTo(t1.getEndTime());
                }
            }
        });

        Pair<String, String> ans=null;

        for(int i=1;i<bookings.size();i++){
            int hours = Integer.parseInt(bookings.get(i).getStartTime().substring(0, 2));
            int minutes = Integer.parseInt(bookings.get(i).getStartTime().substring(2, 4));

            int prevHours = Integer.parseInt(bookings.get(i-1).getEndTime().substring(0, 2));
            int prevMinutes= Integer.parseInt(bookings.get(i-1).getEndTime().substring(2, 4));

            if(hours - prevHours > reqHours || ( (hours-prevHours) == reqHours && (minutes-prevMinutes) > reqMinutes ) ){
                int minutesPresent = (hours-prevHours)*60 + minutes - prevMinutes;

                if(startHours < prevHours && endHours < hours){
                    if( endHours - prevHours > reqHours ){
                        int tempMinutes = prevMinutes + reqMinutes;
                        Pair<String, String> temp = new Pair<>(prevHours + "" + prevMinutes, (startHours + reqHours + tempMinutes/60) +""+ (tempMinutes%60) );
                        ans = temp;
                        break;
                    }
                } else if(startHours > prevHours && endHours < hours){
                    int tempMinutes = startMinutes + reqMinutes;
                    Pair<String, String> temp = new Pair<>(startHours + ""+ startMinutes, (startHours + reqHours + tempMinutes/60) +"" + (tempMinutes%60));
                    ans = temp;
                    break;
                } else if(startHours > prevHours && endHours > hours){
                    if( hours - startHours > reqHours ){
                        int tempMinutes = startMinutes + reqMinutes;
                        Pair<String, String> temp = new Pair<>(startHours + ""+ startMinutes, (startHours + reqHours + tempMinutes/60) +"" + (tempMinutes%60));
                        ans = temp;
                        break;
                    }
                }

            }
        }

        return ans;
    }


    public Pair<String, String> getTimeAlloted(ArrayList<Booking> tempBookings, String startTime, int totalTime){

        ArrayList<Booking> bookings = tempBookings;

        int startHours = Integer.parseInt(startTime.substring(0,2));
        int startMinutes = Integer.parseInt(startTime.substring(2, 4));

        int reqHours = totalTime/60;
        int reqMinutes = totalTime%60;

//        Booking tempBook = new Booking();
//        tempBook.setEndTime("0000");
//        tempBook.setStartTime("0800");
//
//        bookings.add(tempBook);
//
//        Booking tempBook2 = new Booking();
//        tempBook2.setStartTime("2000");
//        tempBook2.setEndTime("2359");
//
//        bookings.add(tempBook2);

        Collections.sort(bookings, new Comparator<Booking>() {
            @Override
            public int compare(Booking booking, Booking t1) {
                if(!booking.getStartTime().equals(t1.getStartTime())){
                    return booking.getStartTime().compareTo(t1.getStartTime());
                } else{
                    return booking.getEndTime().compareTo(t1.getEndTime());
                }
            }
        });

        Pair<String, String> ans=null;

        for(int i=1;i<bookings.size();i++){
            int hours = Integer.parseInt(bookings.get(i).getStartTime().substring(0, 2));
            int minutes = Integer.parseInt(bookings.get(i).getStartTime().substring(2, 4));

            int prevHours = Integer.parseInt(bookings.get(i-1).getEndTime().substring(0, 2));
            int prevMinutes= Integer.parseInt(bookings.get(i-1).getEndTime().substring(2, 4));

            int minutesgap = (hours-prevHours)*60 + minutes - prevMinutes;

            if(totalTime <= minutesgap){
                if(startHours < prevHours || (startHours == prevHours && startMinutes < prevMinutes)){{
                    int tempMinutes = prevMinutes + reqMinutes;
                    int nextHours = prevHours + reqHours + tempMinutes/60;
                    int nextMinutes = tempMinutes%60;
                    Pair<String, String> temp = new Pair<>( String.valueOf(prevHours < 10 ? "0"+prevHours : prevHours) + "" + String.valueOf(prevMinutes<10 ? "0"+prevMinutes : prevMinutes ) ,
                            String.valueOf( nextHours < 10 ? "0" + nextHours : nextHours ) +"" + String.valueOf( nextMinutes < 10 ? "0" + nextMinutes : nextMinutes ) );

                    ans = temp;
                    break;
                }} else if( ( startHours > prevHours || (startHours==prevHours && startMinutes>=prevMinutes) ) && ( startHours < hours || ( startHours==hours && startMinutes < minutes ) ) ){
                    int minutesLeftInGap = (hours - startHours)*60 + minutes - startMinutes;
                    if(minutesLeftInGap > totalTime){
                        int tempMinutes = reqMinutes + startMinutes;
                        int nextHours = startHours + reqHours + tempMinutes/60;
                        int nextMinutes = tempMinutes%60;
                        Pair<String, String> temp = new Pair<>( String.valueOf(startHours < 10 ? "0"+startHours : startHours) + "" + String.valueOf(startMinutes<10 ? "0"+startMinutes : startMinutes) ,
                                String.valueOf( nextHours < 10 ? "0" + nextHours : nextHours ) +"" + String.valueOf( nextMinutes < 10 ? "0" + nextMinutes : nextMinutes ) );
                        ans=temp;
                        break;
                    }

                }
            }
//            if(hours - prevHours > reqHours || ( (hours-prevHours) == reqHours && (minutes-prevMinutes) >= reqMinutes ) ){
//
//
//                if(startHours < prevHours || (startHours == prevHours && startMinutes < prevMinutes)){
//                    int tempMinutes = prevMinutes + reqMinutes;
//                    int nextHours = prevHours + reqHours + tempMinutes/60;
//                    int nextMinutes = tempMinutes%60;
//                    Pair<String, String> temp = new Pair<>( String.valueOf(prevHours < 10 ? "0"+prevHours : prevHours) + "" + String.valueOf(prevMinutes<10 ? "0"+prevMinutes : prevMinutes ) ,
//                            String.valueOf( nextHours < 10 ? "0" + nextHours : nextHours ) +"" + String.valueOf( nextMinutes < 10 ? "0" + nextMinutes : nextMinutes ) );
//
//                    ans = temp;
//                    break;
//                } else if( ( startHours > prevHours || (startHours==prevHours && startMinutes>=prevMinutes) ) && ( startHours < hours || ( startHours==hours && startMinutes < minutes ) ) ){
//                    if( ( hours - startHours > reqHours ) || ( hours - startHours == reqHours && minutes - startMinutes >= reqMinutes ) ){
//                        int tempMinutes = reqMinutes + startMinutes;
//                        int nextHours = startHours + reqHours + tempMinutes/60;
//                        int nextMinutes = tempMinutes%60;
//                        Pair<String, String> temp = new Pair<>( String.valueOf(startHours < 10 ? "0"+startHours : startHours) + "" + String.valueOf(startMinutes<10 ? "0"+startMinutes : startMinutes) ,
//                                String.valueOf( nextHours < 10 ? "0" + nextHours : nextHours ) +"" + String.valueOf( nextMinutes < 10 ? "0" + nextMinutes : nextMinutes ) );
//                        ans=temp;
//                        break;
//                    }
//                }
//            }
        }

        return ans;
    }

}
