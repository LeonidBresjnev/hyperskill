class Main {
    public static void main(String[] args) {
        int sum = 0;
        String[] levels = new java.util.Scanner(System.in).nextLine().toUpperCase().split(" ");
   /*     for (String level : levels) {
            sum += java.util.logging.Level.parse(level).intValue();
        }
        System.out.print(sum);*/
        sum = 0;
        for (String level : levels) {
            switch (level) {
                case "SERVERE": {
                    sum += 1000;
                    break;
                }
                case "WARNING": {
                    sum += 900;
                    break;
                }
                case "INFO": {
                    sum += 800;
                    break;
                }
                case "CONFIG": {
                    sum += 700;
                    break;
                }
                case "FINE": {
                    sum += 500;
                    break;
                }
                case "FINER": {
                    sum += 400;
                    break;
                }
                case "FINEST": {
                    sum += 300;
                    break;
                }
            }
        }
    }
}
