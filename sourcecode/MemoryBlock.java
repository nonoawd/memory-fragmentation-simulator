import java.util.*;

class MemoryBlock {
    int size, start, end, fragmentation;
    String process;
    boolean status;

    MemoryBlock(int size, int start) {
        this.size = size;
        this.start = start;
        this.end = start + size - 1;
        this.process = "Null";
        this.status = false;
        this.fragmentation = 0;
    }
}
