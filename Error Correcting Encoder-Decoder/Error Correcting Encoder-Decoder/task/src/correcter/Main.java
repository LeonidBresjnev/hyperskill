package correcter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    static void encoder(String infile, String outfile) {
        try {
            new File(outfile).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            java.util.Scanner myScanner = new java.util.Scanner(new File(infile));

            String myString = myScanner.nextLine();
            myScanner.close();
            System.out.println(infile + "\n" + myString);

            System.out.print("hex view:");
            FileOutputStream fileOutputStream = new FileOutputStream("encoded.txt");

            byte[] myBytes= myString.getBytes();

            for (byte myByte : myBytes) {
                System.out.print(" " + Integer.toHexString(myByte));
            }
            System.out.print("\nbin view:");
            for (byte myByte : myBytes) {
                System.out.print(" " + String.format("%8s", Integer.toBinaryString(myByte)).replace(' ', '0'));
            }

            System.out.print("\n\nencoded.txt\nexpand:   ");
            int k = 0;
            int nextparity = 1;

            short hex = 0;
            List<Short> hexlist = new ArrayList<>();
            for (byte myByte : myBytes) {
                for (int i = 7; i >= 0; i--) {
                    while (k + 1 == nextparity) {
                        System.out.print(".");
                        nextparity <<= 1;
                        k++;
                        if (k == 8) {
                            System.out.print(" ");
                            nextparity = 1;
                            k = 0;
                            hexlist.add(hex);
                            hex = 0;
                        }
                    }
                    System.out.print((myByte >> i) % 2);
                    hex += (((myByte >> i) % 2) << (7-k));

                    k++;
                    if (k == 8) {
                        System.out.print(" ");
                        nextparity = 1;
                        k = 0;
                        hexlist.add(hex);
                        hex = 0;
                    }
                }
            }

            if (k < 8) {
                System.out.print("0".repeat(8 - k));
                hexlist.add(hex);
            }

            for (int i = 0; i < hexlist.size(); i++) {
                hex = hexlist.get(i);
                hex += ((((hex >> 1) & 1) ^ ((hex >> 3) & 1) ^ ((hex >> 5) & 1)) << 7)
                        +  ((((hex >> 1) & 1) ^ ((hex >> 2) & 1) ^ ((hex >> 5) & 1)) << 6)
                        +  ((((hex >> 1) & 1) ^ ((hex >> 2) & 1) ^ ((hex >> 3) & 1)) << 4);
                hexlist.set(i, hex);
            }
            System.out.print("\nbin view:");
            for (short myHex : hexlist){
                fileOutputStream.write((byte)Short.toUnsignedInt(myHex));
                System.out.print(" "+  String.format("%8s",Integer.toBinaryString(myHex)).replace(' ', '0'));
            }
            System.out.print("\nhex view:");
            for (short myHex : hexlist){
                System.out.print(" "+  String.format("%2s",Integer.toHexString(myHex)).replace(' ', '0'));
            }

            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void send(String infile, String outfile) {
        try {
            java.util.Scanner myScanner = new java.util.Scanner(new File(infile));
            List<Short> hexList = new ArrayList<>();
            List<Short> noised = new ArrayList<>();

            try {
                new File(outfile).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(outfile);
            byte[] myBytes = Files.readAllBytes(Paths.get(infile));
            for (byte myByte : myBytes) {
                hexList.add((short)Byte.toUnsignedInt(myByte));
            }

            System.out.print(infile + "\nhex view:");
            hexList.forEach(x -> System.out.print(" " + String.format("%2s", Integer.toHexString(x)).replace(' ', '0')));
            System.out.print("\nbin view:");
            hexList.forEach(x -> System.out.print(" " + String.format("%8s", Integer.toBinaryString(x)).replace(' ', '0')));

            System.out.print("\n\n" + outfile + "\nbin view:");
            for (Short aShort : hexList) {
                short decoded = (short) (aShort ^ ((short) 1 << (int) (8 * Math.random())));
                noised.add(decoded);
                System.out.print(" " + String.format("%8s", Integer.toBinaryString(decoded)).replace(' ', '0'));
            }
            System.out.print("\nhex view:");

            String hexstring;
            for (Short aShort : noised) {
                fileOutputStream.write((byte) Short.toUnsignedInt(aShort));
                hexstring = String.format("%2s", Integer.toHexString(aShort)).replace(' ', '0');
                System.out.print(" " + hexstring);
            }
            myScanner.close();

            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void decoder(String infile, String outfile) {
        try {
            java.util.Scanner myScanner = new java.util.Scanner(new File(infile));
            List<Short> hexList = new ArrayList<>();
            List<Short> corrected = new ArrayList<>();
            List<Short> decoded = new ArrayList<>();
            try {
                new File(outfile).createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.print(infile + "\nhex view:");
            byte[] myBytes = Files.readAllBytes(Paths.get(infile));
            for (byte myByte : myBytes) {
                hexList.add((short)Byte.toUnsignedInt(myByte));
            }
            hexList.forEach(x -> System.out.print(" " + String.format("%2s", Integer.toHexString(x)).replace(' ', '0')));
            System.out.print("\nbin view:");
            hexList.forEach(x -> System.out.print(" " + String.format("%8s", Integer.toBinaryString(x)).replace(' ', '0')));

            //Decode;
            System.out.print("\n\n" + outfile + "\ncorrect:");
            int k = 0;

            for (short aShort : hexList){
                k = (((aShort >> 1) & 1) ^ ((aShort >> 3) & 1) ^ ((aShort >> 5) & 1) ^ ((aShort >> 7) & 1))
                        + (((aShort >> 1) & 1) ^ ((aShort >> 2) & 1) ^ ((aShort >> 5) & 1) ^ ((aShort >> 6) & 1))*2
                        + (((aShort >> 1) & 1) ^ ((aShort >> 2) & 1) ^ ((aShort >> 3) & 1) ^ ((aShort >> 4) & 1))*4;
                short hex = 0;
                if (k>0) {
                    hex = (short) ( aShort ^ (1 << (8-k)));
                } else {
                    hex = aShort;
                }
                System.out.print(" " + String.format("%8s", Integer.toBinaryString(hex)).replace(' ', '0'));
                corrected.add(hex);
            }
            k = 0;
            short hex = 0;
            for (int i = 0; i< corrected.size(); i++) {
                for (int j = 3; j<=7; j++){
                    if (j != 4) {
                        hex += ((corrected.get(i) >> (8-j)) & 1) * (1 << 7-k);
                        k++;
                        if (k == 8) {
                            decoded.add(hex);
                            hex = 0;
                            k = 0;
                        }
                    }
                }
            }

            System.out.print("\ndecode:");
            for (short aShort : decoded){
                System.out.print(" " + String.format("%8s", Integer.toBinaryString(aShort)).replace(' ', '0'));
            }
            while (decoded.get(decoded.size()-1) == 0) {
                decoded.remove(decoded.size()-1);
            }
            System.out.print("\nremove:");
            for (short aShort : decoded){
                System.out.print(" " + String.format("%8s", Integer.toBinaryString(aShort)).replace(' ', '0'));
            }

            System.out.print("\nhex view:");
            for (short aShort : decoded){
                System.out.print(" " + String.format("%2s", Integer.toHexString(aShort)).replace(' ', '0'));
            }

            System.out.print("\ntext view: ");

            FileOutputStream fileOutputStream = new FileOutputStream(outfile);
            for (short aShort : decoded){
                System.out.print((char) aShort);

                fileOutputStream.write((char) aShort);
            }

            myScanner.close();

            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Write a mode: ");
        String mode = new java.util.Scanner(System.in).nextLine();

        if ("encode".equalsIgnoreCase(mode)) {
            encoder("send.txt","encoded.txt");
        } else if ("send".equalsIgnoreCase(mode)) {
            send("encoded.txt","received.txt");
        } else if ("decode".equalsIgnoreCase(mode)) {
            decoder("received.txt","decoded.txt");
        }
    }
}