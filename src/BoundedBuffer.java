class BoundedBuffer<T> {
    private final T[] buffer;
    private int count = 0;
    private int in = 0;
    private int out = 0;

    @SuppressWarnings("unchecked")
    public BoundedBuffer(int size) {
        buffer = (T[]) new Object[size];
    }

    public synchronized void put(T item) throws InterruptedException {
        // TODO: Реализуйте метод
        while (count == buffer.length){
            wait();
        }
        buffer[in] = item;
        in = (in + 1) % buffer.length;
        count ++;
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        // TODO: Реализуйте метод
        while (count == 0) {
            wait();
        }
        T item = buffer[out];
        count --;
        out = (out + 1) % buffer.length;
        notifyAll();
        return item;
    }
}
