package processor;

public class Main {
    static java.util.Scanner myreader = new java.util.Scanner(System.in);

    static double[][] readMatrix() {
        double[][] A = new double[myreader.nextInt()][myreader.nextInt()];
        // myreader.nextLine();
        System.out.println("Enter matrix:");
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] = myreader.nextDouble();
            }
        }
        return A;
    }

    static void addMatrix(){
        System.out.println("Enter size of first matrix:");
        double[][] A = readMatrix();
        System.out.println("Enter second matrix:");
        double[][] B = readMatrix();
        if (B.length == A.length && B[0].length == A[0].length) {
            double[][] C = new double[A.length][A[0].length];
            for (int i = 0; i < A.length ; i++){
                for (int j = 0; j < A[0].length ; j++){
                    C[i][j] = A[i][j] + B[i][j];
                }
            }
            printMatrix(C);
        } else {
            System.out.println("ERROR");
        }
    }


    static void multMatrix(){
        System.out.println("Enter size of first matrix:");
        double[][] A = readMatrix();
        System.out.println("Enter second matrix:");
        double[][] B = readMatrix();
        if (B.length == A[0].length) {
            double[][] C = new double[A.length][B[0].length];
            for (int i = 0; i < A.length ; i++){
                for (int j = 0; j < B[0].length ; j++) {
                    C[i][j] = 0;
                    for (int k = 0; k < A[0].length; k++) {
                        C[i][j] += A[i][k] * B[k][j];
                    }
                }
            }
            printMatrix(C);
        } else {
            System.out.println("ERROR");
        }
    }

    static void printMatrix(double[][] A){
        for (double[] ints : A) {
            for (double anInt : ints) {
                if (anInt-(int) anInt == 0f){
                    System.out.printf("%d ", (long) anInt);
                } else{
                    System.out.printf("%f ", anInt);
                }
            }
            System.out.print("\n");
        }
        System.out.println();
    }

    static void scalarMult(){
        System.out.println("Enter size of matrix:");
        double[][] A = readMatrix();
        System.out.println("Enter constant:");
        double c = myreader.nextDouble();
        double[][] cA = new double[A.length][A[0].length];
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                cA[i][j] = c * A[i][j] ;
            }
        }
        printMatrix(cA);
    }
    static void determinant() {
        System.out.println("Enter size of matrix:");
        double[][] A = readMatrix();
        System.out.println(determinantDoIt(A));
    }
    static double determinantDoIt(double[][] A) {
        if (A.length == 1) {
            return A[0][0];
        } else {
            double det = 0d;
            for (int i = 0; i < A.length; i++) {
                double[][] B = new double[A.length-1][A.length-1];
                for (int k = 0; k < i; k++) {
                    System.arraycopy(A[k], 1, B[k], 0, A[k].length-1);
                }
                for (int k = i + 1; k < A.length; k++) {
                    System.arraycopy(A[k], 1, B[k - 1], 0, A[k].length-1);
                }
                det += A[i][0] * determinantDoIt(B) * Math.pow(-1,i);
            }
            return det;
        }
    }


    static void inverse() {
        System.out.println("Enter size of matrix:");
        double[][] A = readMatrix();
        double det = determinantDoIt(A);
        if (Math.abs(det) < 1E-6) {
            System.out.println("not invertible!");
        } else {
            double[][] B = new double[A.length-1][A.length-1];
            double[][] adj = new double[A.length][A.length];
            for (int i = 0; i < A.length; i++) {
                for (int j = 0; j < A.length; j++) {
                    for (int k = 0; k < i; k++) {
                        System.arraycopy(A[k], 0, B[k], 0, j);
                        System.arraycopy(A[k], j + 1, B[k], j , A[k].length - 1 - j);
                    }
                    for (int k = i + 1; k < A.length; k++) {
                        System.arraycopy(A[k], 0, B[k - 1], 0, j);
                        System.arraycopy(A[k], j + 1, B[k - 1], j, A[k].length - 1 - j);
                    }
                    adj[j][i] = Math.pow(-1,i + j) * determinantDoIt(B)/det;
                }
            }
            System.out.println("The result is:");
            printMatrix(adj);
        }
    }

    static void transMatrix(){
        System.out.println("1. Main diagonal\n" +
                "2. Side diagonal\n" +
                "3. Vertical line\n" +
                "4. Horizontal line");
        short way = myreader.nextShort();
        System.out.println("Enter matrix size:");
        double[][] A = readMatrix();
        System.out.println("Enter constant:");
        double[][] B;

        switch (way) {
            case (1): {
                B = new double[A[0].length][A.length];
                for (int i = 0; i < A.length; i++){
                    for (int j = 0; j < A.length; j++){
                        B[j][i] = A[i][j];
                    }
                }
                break;
            }
            case (2): {
                B = new double[A[0].length][A.length];
                for (int i = 0; i < A.length; i++){
                    for (int j = 0; j < A.length; j++){
                        B[A.length - 1 - j][A.length - 1 - i] = A[i][j];
                    }
                }
                break;
            }
            case (4): {
                B = new double[A.length][A[0].length];
                for (int i = 0; i < A.length; i++){
                    System.arraycopy(A[i], 0, B[A.length - 1 - i], 0, A[i].length);
                }
                break;
            }
            case (3): {
                B = new double[A.length][A[0].length];
                for (int i = 0; i < A.length; i++){
                    for (int j = 0; j < A[0].length; j++){
                        B[i][A[0].length - 1 - j] = A[i][j];
                    }
                }
                break;
            }
            default :  {
                B = new double[A[0].length][A.length];
                for (int i = 0; i < A.length; i++){
                    System.arraycopy(A[i], 0, B[i], 0, A[i].length);
                }
                break;
            }
        }

        printMatrix(B);
    }

    static boolean takeInput() {
        String input =  myreader.next();
        if ("0".equals(input)) {
            return false;
        } else if ("1".equals(input)) {
            addMatrix();
            return true;
        } else if ("2".equals(input)) {
            scalarMult();
            return true;
        } else if ("3".equals(input)) {
            multMatrix();
            return true;
        }  else if ("4".equals(input)) {
            transMatrix();
            return true;
        }  else if ("5".equals(input)) {
            determinant();
            return true;
        }   else if ("6".equals(input)) {
            inverse();
            return true;
        } else {
            return true;
        }
    }

    public static void main(String[] args) {
        do {
            System.out.println("1. Add matrices\n" +
                    "2. Multiply matrix by a constant\n" +
                    "3. Multiply matrices\n" +
                    "4. Transpose matrix\n" +
                    "5. Calculate a determinant\n" +
                    "6. Inverse matrix\n" +
                    "0. Exit");
        } while (takeInput());
    }
}
