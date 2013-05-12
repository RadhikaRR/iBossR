package com.bb.customFields;

public class StringSplitter {

	public static StringSplitter INSTANCE = new StringSplitter();

	public String[] split(String strString, String strDelimiter) {

		int iOccurrences = 0;
		int iIndexOfInnerString = 0;
		int iIndexOfDelimiter = 0;
		int iCounter = 0; // Check for null input strings.

		if (strString.startsWith(strDelimiter)) {
			strString = strString.substring(strDelimiter.length());
		} // If strString does not end with the
		// delimiter then add it // to the string in order to comply with // the
		// desired format.

		if (!strString.endsWith(strDelimiter)) {
			strString += strDelimiter;
		} // Count occurrences of the delimiter in // the string. // Occurrences
		// should be the same amount
		// of inner strings.

		while ((iIndexOfDelimiter = strString.indexOf(strDelimiter, iIndexOfInnerString)) != -1) {
			iOccurrences += 1;
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
		} // Declare the array with the correct // size.

		String[] strArray = new String[iOccurrences]; // Reset the indices.
		iIndexOfInnerString = 0;
		iIndexOfDelimiter = 0; // Walk across the string again and this // time
		// add the
		// strings to the array.

		while ((iIndexOfDelimiter = strString.indexOf(strDelimiter, iIndexOfInnerString)) != -1) {
			// Add string to //array.
			strArray[iCounter] = strString.substring(iIndexOfInnerString, iIndexOfDelimiter);
			// Increment the // index to the next // character after
			// the next // delimiter.
			iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
			// Inc the counter.
			iCounter += 1;
		}
		return strArray;
	}
}