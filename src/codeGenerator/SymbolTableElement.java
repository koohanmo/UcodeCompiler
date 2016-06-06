package codeGenerator;


/**
 * �� ������ U-code�� sym���� ���ǵɶ� �ʿ��� ��� ������ ����ִ� ���� ���� Ŭ����.
 *
 * @see Syntax.Program
 */
public class SymbolTableElement {
	private final int blockNum;
	private final int startAddress;
	private final int size;

	public SymbolTableElement(int blockNum, int startAddress, int size) {
		this.blockNum = blockNum;
		this.startAddress = startAddress;
		this.size = size;
	}

	public int getBlockNum() {
		return blockNum;
	}

	public int getStartAddress() {
		return startAddress;
	}

	public int getSize() {
		return size;
	}
}

