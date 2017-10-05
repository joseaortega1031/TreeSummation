import mpi.*;

class SumTree {
    static public void main(String[] args) throws MPIException {

	MPI.Init(args);

	int Max = 16;
	int Sum = 0;
	int SqSum = 0;
	int Tag = 0;
	int myID = MPI.COMM_WORLD.getRank();
	int Pcount = MPI.COMM_WORLD.getSize() ;

	for(int i = 0; i < Max; i++) SqSum += i;
	System.out.println("ID: " + myID + " My SqSum is: " + SqSum);

	int start = (myID % Pcount) * (Max / Pcount);
	int end = (((myID % Pcount) + 1) * (Max / Pcount)) - 1;
	int firstmyID=(myID*2)+1;
	int secondmyID=(myID*2)+2;
	int sendMyID=(myID-1)/2;

	for(int i = start; i <= end; i++)
	{
	   Sum += i;
	   System.out.println("ID: " + myID + " My Sum is now: " + Sum);
//	   System.out.println("My ID: "+myID+"  multiplied by two plus 1: " + firstmyID);
//	   System.out.println("MY ID " +myID+"multiplied by two plus 2: "+ secondmyID);
	}

	int message[] = new int [1];

	if(firstmyID<Pcount)
	{
	   MPI.COMM_WORLD.recv(message, 1, MPI.INT, firstmyID, Tag);
	   Sum +=message[0];
	   System.out.println("ID: " +myID+ " received: " + message[0]+ " from " +firstmyID+ "My Sum is now: "+ Sum);
	}

	if(secondmyID<Pcount)
	{
	   MPI.COMM_WORLD.recv(message, 1, MPI.INT, secondmyID, Tag);
	   Sum +=message[0];
	   System.out.println("ID: " + myID + " Recv Sum: " + message[0] + " From ID: " + secondmyID + " My Sum is now: " + Sum);
	}

        if(sendMyID>=0)
        {
        message[0] =Sum;
        MPI.COMM_WORLD.send(message, 1, MPI.INT, sendMyID,Tag);
        System.out.println("ID: " +myID+" sent : "+Sum+ "to ID:  "+sendMyID);

        }
	MPI.Finalize();
    }
}
