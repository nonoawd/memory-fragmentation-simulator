import java.util.*;

class MemorySimulator {
    static List<MemoryBlock> memory = new ArrayList<>();
    static int method;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter total number of blocks: ");
        int total = in.nextInt();
        int addr = 0;

        System.out.println("Enter block sizes in KB:");
        for (int i = 0; i < total; i++) {
            int sz = in.nextInt();
            memory.add(new MemoryBlock(sz, addr));
            addr += sz;
        }

        System.out.print("Choose strategy (1 = First-Fit, 2 = Best-Fit, 3 = Worst-Fit): ");
        method = in.nextInt();

        int input;
        do {
            System.out.println("============================================");
            System.out.println("1) Allocate memory");
            System.out.println("2) Deallocate memory");
            System.out.println("3) View memory status");
            System.out.println("4) Exit");
            System.out.println("============================================");
            System.out.print("Enter choice: ");
            input = in.nextInt();

            switch (input) {
                case 1 -> {
                    System.out.print("Enter Process ID and size: ");
                    String id = in.next();
                    int s = in.nextInt();
                    allocate(id, s);
                }
                case 2 -> {
                    System.out.print("Enter Process ID to free: ");
                    String id = in.next();
                    release(id);
                }
                case 3 -> showStatus();
                case 4 -> System.out.println("Simulation ended.");
                default -> System.out.println("Invalid option.");
            }
        } while (input != 4);
    }

    static void allocate(String id, int sz) {
        MemoryBlock selected = null;

        switch (method) {
            case 1 -> { // First-Fit
                for (MemoryBlock b : memory) {
                    if (!b.status && b.size >= sz) {
                        selected = b;
                        break;
                    }
                }
            }
            case 2 -> { // Best-Fit
                int min = Integer.MAX_VALUE;
                for (MemoryBlock b : memory) {
                    if (!b.status && b.size >= sz && b.size < min) {
                        selected = b;
                        min = b.size;
                    }
                }
            }
            case 3 -> { // Worst-Fit
                int max = -1;
                for (MemoryBlock b : memory) {
                    if (!b.status && b.size >= sz && b.size > max) {
                        selected = b;
                        max = b.size;
                    }
                }
            }
            default -> System.out.println("Unknown strategy.");
        }

        if (selected != null) {
            selected.status = true;
            selected.process = id;
            selected.fragmentation = selected.size - sz;
            System.out.println(id + " allocated at address " + selected.start +
                    " with internal fragmentation = " + selected.fragmentation);
        } else {
            System.out.println("Insufficient memory for process " + id);
        }
    }


    static void release(String id) {
        boolean freed = false;
        for (MemoryBlock b : memory) {
            if (b.status && b.process.equals(id)) {
                b.status = false;
                b.process = "Null";
                b.fragmentation = 0;
                freed = true;
                System.out.println("Process " + id + " removed from memory.");
                break;
            }
        }
        if (!freed) System.out.println("Process not found.");
    }

    static void showStatus() {
        System.out.println("===============================================================");
        System.out.println("Block#  Size   Start-End     Status      Process     Fragment");
        System.out.println("===============================================================");
        for (int i = 0; i < memory.size(); i++) {
            MemoryBlock b = memory.get(i);
            System.out.printf("B%-6d %-6d %d-%-12d %-11s %-10s %d\n",
                    i, b.size, b.start, b.end,
                    b.status ? "allocated" : "free",
                    b.process, b.fragmentation);
        }
        System.out.println("===============================================================");
    }
}
