import java.util.ArrayList;

public class SecondPass {
	public static ArrayList<ArrayList<String>> listingFile = new ArrayList<>();
	public static ArrayList<String> objectCode = new ArrayList<>();
	public static int lineCounter = 0;
	public static String operandAddress;
	public static String code;
	public static String exeAddress;

	public static boolean secondPass(String Statment) throws Exception {
		String[] arr = Statment.split(",");
        String address = arr[0];
		if (arr.length == 2) {
			FirstPass.setStatmentTable(arr[1]);
		} else {
			FirstPass.setStatmentTable(arr[1] + "," + arr[2]);

		}
		listingFile.add(new ArrayList<String>());

		String label = FirstPass.statmentTable[0];
		String opCode = FirstPass.statmentTable[1];
		String operand = FirstPass.statmentTable[2];
		label = label.trim();
		opCode = opCode.trim();
		operand = operand.trim();

		if (opCode.equals("START") && lineCounter == 0) {
			listingFile.get(0).add(FirstPass.startAddress);
			listingFile.get(0).add(label);
			listingFile.get(0).add(opCode);
			listingFile.get(0).add("0");
			listingFile.get(0).add("");
			return false;
		} else if (!opCode.equals("END")) {
			lineCounter++;
			if (true) {// we didn't handle comments
				code = FirstPass.getsearchOpTable(opCode);
				if (code != null) {
					if (operand.length() != 0) {// 18 space
                        operandAddress = FirstPass
                                .searchSymbolTable(
                                        operand.split(",")[0]);

						if (operandAddress != null) {
                            // operandAddress = operandAddress.format("%1$04d",
                            // Integer.parseInt(operandAddress, 16));

							listingFile.get(lineCounter).add(address);
							listingFile.get(lineCounter).add(label);
							listingFile.get(lineCounter).add(opCode);
							listingFile.get(lineCounter).add(operand);


						} else {
							operandAddress = "0000";
							listingFile.get(lineCounter).add(address);
							listingFile.get(lineCounter).add(label);
							listingFile.get(lineCounter).add(opCode);
							listingFile.get(lineCounter).add(operand);
							listingFile.get(lineCounter).add(operandAddress);
							listingFile.get(lineCounter).add("undefined symbol");
						}
					} else {
						operandAddress = "0000";
						listingFile.get(lineCounter).add(address);
						listingFile.get(lineCounter).add(label);
						listingFile.get(lineCounter).add(opCode);
						listingFile.get(lineCounter).add(operand);
						listingFile.get(lineCounter).add(code + operandAddress);

					}
					if (operand.contains(",X")) {
						
                        Integer outputDecimal = Integer
                                .parseInt(operandAddress, 16);
//                        System.out.println(operandAddress + "   "
//                                + outputDecimal);
                        operandAddress = FirstPass
                                .increaseAddress(outputDecimal
                                        .toString(),
                                        8000);
                        // operandAddress = operandAddress.format("%1$04d",
                        // Integer.parseInt(operandAddress, 16));
					}
					listingFile.get(lineCounter).add(code + "" + operandAddress);
				} else if (opCode.equals("BYTE") || opCode.equals("WORD")) {

					listingFile.get(lineCounter).add(address);
					listingFile.get(lineCounter).add(label);
					listingFile.get(lineCounter).add(opCode);
					listingFile.get(lineCounter).add(operand);
					String s = Integer.toHexString((Integer.decode(operand)));
					s = s.format("%1$06d", Integer.parseInt(s, 16));
					listingFile.get(lineCounter).add(s);

				} else if (opCode.equals("RESW") || opCode.equals("RESB")) {
					listingFile.get(lineCounter).add(address);
					listingFile.get(lineCounter).add(label);
					listingFile.get(lineCounter).add(opCode);
					listingFile.get(lineCounter).add(operand);
					listingFile.get(lineCounter).add("");
//                    System.out.println(listingFile
//                            .get(lineCounter)
//                            + "               mmmmmmmmmmmmmmmm");

				}
			}

		} else {
            lineCounter++;
			listingFile.get(lineCounter).add(address);
            listingFile.get(lineCounter).add(opCode);
			listingFile.get(lineCounter).add(operand);
			exeAddress = FirstPass.searchSymbolTable(operand);
			listingFile.get(lineCounter).add("");
			return true;
		}
		return false;

	}

	public static void writeObjectCode() {
		String code = "";
		FirstPass.startAddress = FirstPass.startAddress.format("%1$06d", Integer.parseInt(FirstPass.startAddress, 16));
		String startAddress = "";

		objectCode.add("H^" + listingFile.get(0).get(1) + "^" + FirstPass.startAddress + "^" + FirstPass.programLength);
		int i = 1;

		int counter = 0;
		while (i < listingFile.size() - 1) {
			if (counter == 0) {
				startAddress = listingFile.get(i).get(0);
				startAddress = startAddress.format("%1$06d", Integer.parseInt(startAddress, 16));
			}
			if (listingFile.get(i).get(4).length() != 0) {
				code += "^" + listingFile.get(i).get(4);
				counter += (listingFile.get(i).get(4).length() / 2);
			}
			i++;
			if (counter == 30) {
				objectCode.add("T^" + startAddress + "^" + Integer.toHexString(counter) + code);
				counter = 0;
				code = "";
                Integer outputDecimal = Integer
                        .parseInt(startAddress, 16);
                startAddress = FirstPass.increaseAddress(
                        outputDecimal.toString(), 30);
			}
		}
		if (counter != 0) {
			String str = Integer.toHexString(counter);
			str = str.format("%1$02d", Integer.parseInt(str, 16));
			objectCode.add("T^" + startAddress + "^" + str + code);
		}
		exeAddress = exeAddress.format("%1$06d", Integer.parseInt(exeAddress, 16));
		objectCode.add("E^" + exeAddress);
	}

	public static void printObjectCode() {
		for (int i = 0; i < objectCode.size(); i++) {
			System.out.println(objectCode.get(i));
		}
	}
/*
	public static void printListingTable() {
		for (int i = 0; i < listingFile.size(); i++) {
			for (int j = 0; j < listingFile.get(i).size(); j++) {
				System.out.print(listingFile.get(i).get(j) + "-");
			}
			System.out.println();
		}

	}
*/
}
