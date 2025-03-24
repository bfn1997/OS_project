import java.util.Scanner;

class MemoryBlock {
    int size;
    int startAddress;
    int endAddress;
    String status; // "allocated" or "free"
    String processID; // "Null" when free
    int internalFragmentation;

    public MemoryBlock(int size, int startAddress) {
        this.size = size;
        this.startAddress = startAddress;
        this.endAddress = startAddress + size - 1;
        this.status = "free";
        this.processID = "Null";
        this.internalFragmentation = 0;
    }
}
public class MemoryInitialization {

    public static void main(String[] args) {
       
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the total number of blocks: ");
        int M = scanner.nextInt();
        int[] sizes = new int[M];

        System.out.print("Enter the size of each block in KB: ");
        for (int i = 0; i < M; i++) {
            sizes[i] = scanner.nextInt();
        }

        MemoryBlock[] memoryBlocks = new MemoryBlock[M];
        int startAddress = 0;
        for (int i = 0; i < M; i++) {
            memoryBlocks[i] = new MemoryBlock(sizes[i], startAddress);
            startAddress += sizes[i];
        }

        // Display memory blocks
        System.out.println("Memory blocks:");
        System.out.println("============================================");
        System.out.println("Block#  Size  Start-End  Status");
        System.out.println("============================================");
         for (int i = 0; i < M; i++) {
            System.out.printf("Block%d  %d KB  %d-%d  %s\n", 
                i, memoryBlocks[i].size, memoryBlocks[i].startAddress, 
                memoryBlocks[i].endAddress, memoryBlocks[i].status);
        }
        System.out.println("============================================");
        
        scanner.close();
    }
}
