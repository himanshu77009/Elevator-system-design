package com.java.Elevator_system_design;

import java.util.*;

//An elevator goes up, it continues to go up until there are no "dropoffs" or "pickup" requests in that direction
public class Elevator {
    private static final int MIN_FLOOR=0;
    private static final int MAX_FLOOR=10;
    private static int processingTime=500;//in milliseconds

    private int currentFloor;
    private Direction currentDirection;

    //keeps track of people waiting K(STARTING FLOOR) : V(LIST OF ALL THE DESTINATION FLOORS FROM THAT FLOOR
    private Map<Integer, List<Integer>> requestedPathsMap;

//Once people at a given floor have board the elevator,
//The currentFloorDestination array--> keeps track of the floors the elevator will visit by setting the appropriate index to true
//Our job is to implement the processFloor(), callElevator(), and moveElevator() functions
//We also have a main function that provides an easy way to manually test your elevator	 implementation

    private Boolean[] currentFloorDestination;

    public Elevator() {
        this.currentFloor=0;//assuming that the lift is staring from the ground floor
        this.currentDirection=Direction.UP;//If at bottom the lift will go up
        this.requestedPathsMap=new HashMap<>();
        this.currentFloorDestination=new Boolean[MAX_FLOOR+1];
        Arrays.fill(this.currentFloorDestination, Boolean.FALSE);
    }

    public void setProcessingTime(int processingTime) {
        Elevator.processingTime=processingTime;
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }

    public Map<Integer, List<Integer>> getRequestedPathsMap(){
        return this.requestedPathsMap;
    }

    public Boolean[] getCurrentFloorDestination() {
        return this.currentFloorDestination;
    }

    public void start()throws InterruptedException{
        currentDirection=Direction.UP;//Assuming the lift is on the ground floor initially

        do {
            System.out.println("----------");
            processFloor(currentFloor);
            System.out.println("----------");
        } while(currentDirection!=Direction.IDLE);

        System.out.println("No one is waiting and"+
                "no one is looking to go anywhere");
        System.out.println("Have fun for now");
    }

    public void lunchTimeElevatorRush() {
        Random random = new Random();
        for(int i=0;i<30;i++) {
            callElevator(random.nextInt(11),
                    random.nextInt(10)+1);
        }

    }

    //TO DO#1
    public void callElevator(int start, int destination) {
        if(isInvalidFloor(start) || isInvalidFloor(destination) || start==destination) {
            System.out.println("YOU HAVE ENTERED INVALID FLOOR. TRY AGAIN!!");
            return;
        }
        if(requestedPathsMap.containsKey(start))//if already start is in the map, add the destination in the list
            requestedPathsMap.get(start).add(destination);
        else {//else add the new key as START with the list containing our DESTINATION
            requestedPathsMap.put(start, new ArrayList<>() {{
                add(destination);
            }});
        }
    }

    //TO DO #2
    private void processFloor(int floor)throws InterruptedException{
        if(currentFloorDestination[floor])//Deboarding if any people who reached this destination floor
            System.out.println("UN-BOARDING at floor:"+floor);
        if(requestedPathsMap.containsKey(floor)) {//Onboarding people on their destination
            System.out.println("Boarding at Floor:"+floor);
            requestedPathsMap.get(floor).forEach(destinationFloor->
                    currentFloorDestination[destinationFloor]=true);//Marked true for next traversals
            requestedPathsMap.remove(floor);//removing the entry from map as we have marked all the destinations
        }
        currentFloorDestination[floor]=false;//Marked false as we are just arrived in the current floor
        moveElevator();
    }

    //TO DO #3
    public void moveElevator()throws InterruptedException{

        //SETTING OF DIRECTION
        //IDLING THE ELEVATOR
        if(!Arrays.asList(currentFloorDestination).contains(true) &&
                requestedPathsMap.isEmpty()) {//Stopping the elevator: checking destination are reached and request list is empty
            currentDirection=Direction.IDLE;//This will break the while loop in our initial start() method
            return;
        }else if(isInvalidFloor(currentFloor + 1)){//SWITCHED TO DOWN DIRECTION WHEN REACHED TOP FLOOR
            currentDirection = Direction.DOWN;
        }else if(isInvalidFloor(currentFloor - 1)){//SWITCHED TO UP DIRECTION WHEN REACHED BOTTOM FLOOR
            currentDirection = Direction.UP;
        }
        switch(currentDirection) {//Move the elevator
            //case UP-> moveUp();
            case UP:{
                moveUp();
                break;
            }
            case DOWN:{
                moveDown();
                break;
            }
            default:{
                System.out.println("Elevator Malfunctioned");
            }
        }
    }

    private void moveUp()throws InterruptedException{
        currentFloor++;
        System.out.println("GOING UP TO"+ currentFloor);
        Thread.sleep(processingTime);
    }

    private void moveDown()throws InterruptedException{
        currentFloor--;
        System.out.println("GOING DOWN TO"+ currentFloor);
        Thread.sleep(processingTime);
    }

    private boolean isInvalidFloor(int floor) {
        return floor <MIN_FLOOR || floor> MAX_FLOOR;
    }



}







































