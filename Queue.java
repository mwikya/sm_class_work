import java.io.PrintWriter;
import java.io.FileReader;
import java.lang.Math;
import java.util.*;

public class Queue {
    //Constants
    final static int Q_LIMIT = 100; //limit on queue length
    final static int BUSY = 1; //mnemonic for server being busy
    final static int IDLE = 0; //idle

    //Status variables
    public static int next_event_type, num_custs_delayed, num_delays_required, num_events, num_in_q, server_status;
    public static double area_num_in_q, area_server_status, mean_interarrival, mean_service, time, time_last_event, total_of_delays;
    public static double[] time_arrival = new double[Q_LIMIT+1];
    public static double[] time_next_event = new double[3];

    //I/O files
    public static Scanner inFile = null;
    public static PrintWriter outFile = null;

    //MAIN METHOD
    public static void main(String[] args){

        //Specify number of events
        num_events = 2;

        //Initialize variables
        mean_interarrival = 0;
        mean_service = 0;
        num_delays_required = 0;

        //Reading values from the file
        try {
            inFile = new Scanner(new FileReader("serverQueue.in"));
            mean_interarrival = inFile.nextFloat();
            mean_service = inFile.nextFloat();
            num_delays_required = inFile.nextInt();
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("File not found");
            System.exit(0);
        }
        System.out.println("Reading File done");
        
        //Writing values to the file
        try {
            outFile = new PrintWriter("serverQueue.out","UTF-8");
            outFile.println("Single-server queueing system\n\n");
            outFile.format("Mean interarrival time %11.3f minutes\n\n", mean_interarrival);
            outFile.format("Mean service time %16.3f minutes\n\n", mean_service);
            outFile.format("Number of customers %d\n\n", num_delays_required);
            
        } catch (Exception e) {
            //TODO: handle exception
            System.out.println("Could not write to file");
            System.exit(0);
        }
        System.out.println("Writing file done");

        //Initialize the simulation
        initialize();
        
        //Run the simulation while more delays are still needed
        
        while(num_custs_delayed < num_delays_required){
            //determine next event
            timing();

            //update time-average statistical accumulators
            update_time_avg_stats();

            //invoke appropriate event function

            switch (next_event_type) {
                case 1:
                    arrive();
                    break;

                case 2:
                    depart();
                    break;

                default:
                    break;
            }
        }

        //Invoke the report generator and end the simulation

        report();

        //close files
        inFile.close();
        outFile.close();
    }

    //Initialize the simulation
    public static void initialize() {
        //Initialize the simulation clock

        time = 0;

        //Initialize the status variables
        server_status = IDLE;
        num_in_q = 0;
        time_last_event = 0.0;

        //Initialize the statistical counters

        num_custs_delayed = 0;
        total_of_delays = 0.0;
        area_num_in_q = 0.0;
        area_server_status = 0.0;

        //Initialize the event list. Since no customers are present, the departure service
        //service completion event is eliminated from consideration

        time_next_event[1] = time + expon(mean_interarrival);
        time_next_event[2] = 1.0e+30; //10 to power 30
    }

    //timing function
    public static void timing()
    {
        int i;
        double min_time_next_event = 1.0e29;

        next_event_type = 0;

        //Determine the type of next event to occur
        for(i = 1; i <= num_events; ++i){
            if (time_next_event[i] < min_time_next_event){
                min_time_next_event = time_next_event[i];
                next_event_type = i;
            }
        }

        //Check to see whether the event list is empty

        if (next_event_type == 0){
            //The event list is empty so stop the simulation
            outFile.format("\nEvent list empty at time %f", time);
            System.exit(0);
        }

        //Event list is not empty so advance the simulation clock

        time = min_time_next_event;
    }

    //Arrival event function
    public static void arrive()
    {
        double delay;

        //Schedule next arrival

        time_next_event[1] = time + expon(mean_interarrival);

        //Check to see whether the server is busy

        if (server_status == BUSY){
            //Server is busy, so increment number of customers in queue
            num_in_q++;

            //Check to see whether an overflow condition exists

            if (num_in_q > Q_LIMIT){
                //The queue has overflowed, so stop the simulation
                outFile.print("\nOverflow of the array at time_arrival at");
                outFile.format(" time %f", time);
                System.exit(0);
            }

            //There is still room in the queue, so store the time of arrival of arriving customer at
            //the new end of time_arrival

            time_arrival[num_in_q] = time;
        }
        else
        {
            /*server is idle, so arriving customer has a delay of zero 
            (The following two statements are for program clarity and do not affect the results
        of the simulation)
            */

            delay = 0.0;
            total_of_delays += delay;

            //increment number of customers delayed, and make server busy

            ++num_custs_delayed;
            server_status = BUSY;

            //schedule departure (service completion)

            time_next_event[2] = time + expon(mean_service);
        }
    }

    //Departure event function
    public static void depart()
    {
        int i;
        double delay;
        
        //Check to see whether the queue is empty

        if (num_in_q == 0){
            //the queue is empty so make the server idle and eliminate the
            //departure (service completion) event from consideration

            server_status = IDLE;
            time_next_event[2] = 1.0e+30;
        }
        else{
            //Queue is nonempty so decrement number of customers in queue
            --num_in_q;

            //Compute the delay of the customer who is beginning service and update the total delay accumulator

            delay = time - time_arrival[1];
            total_of_delays += delay;

            //Increment the number of customers delayed and schedule departure

            ++num_custs_delayed;
            time_next_event[2] = time + expon(mean_service);

            //move each customer in queue (if any) up one place

            for(i=1; i <= num_in_q; ++i){
                time_arrival[i] = time_arrival[i+1];
            }
        }

    }

    public static void report() {

        //compute and write estimates of desired measures of performance
        outFile.format("\n\nAverage delay in queue %11.3f minutes\n\n", total_of_delays / num_custs_delayed);
        outFile.format("Average number in queue%10.3f\n\n", area_num_in_q / time);
        outFile.format("Server utilization%15.3f\n\n", area_server_status / time);
        outFile.format("Time simulation ended%12.3f", time);        
    }

    public static void update_time_avg_stats() {
        //update area accumulators for time-average statistics

        double time_since_last_event;

        //compute the time since last event and update last-event-time marker

        time_since_last_event = time - time_last_event;
        time_last_event = time;

        //update area under number-in-queue function

        area_num_in_q += num_in_q*time_since_last_event;
        //update the area under number busy indicator function
        area_server_status += server_status * time_since_last_event;
    }
    //Exponential variate generation function
    public static double expon(double mean)
    {
        double u;

        //Generate a U(0,1) random variable

        u = Math.random();

        //return an exponential random variate with mean "mean"
        return -mean * Math.log(u);

    }
}