package com.bb.customFields;

import java.util.NoSuchElementException;

/**
 * This is a copy of the StringTokenizer in JDK 1.4.2.
 */
public class StringTokenizer {

	public static final String DEFAULT_DELIM = " \t\n\r\f";

	private int currentPosition;

	private String delimiters;

	private boolean delimsChanged;

	/**
	 * maxDelimChar stores the value of the delimiter character with the highest
	 * value. It is used to optimize the detection of delimiter characters.
	 */
	private char maxDelimChar;

	private int maxPosition;

	private int newPosition;

	private boolean retDelims;

	private String str;

	/**
	 * Constructs a string tokenizer for the specified string. The tokenizer
	 * uses the default delimiter set, which is
	 * <code>"&nbsp;&#92;t&#92;n&#92;r&#92;f"</code>: the space character, the
	 * tab character, the newline character, the carriage-return character, and
	 * the form-feed character. Delimiter characters themselves will not be
	 * treated as tokens.
	 * 
	 * @param str
	 *            a string to be parsed.
	 */
	public StringTokenizer(String str) {
		this(str, DEFAULT_DELIM, false);
	}

	/**
	 * Constructs a string tokenizer for the specified string. The characters in
	 * the <code>delim</code> argument are the delimiters for separating tokens.
	 * Delimiter characters themselves will not be treated as tokens.
	 * 
	 * @param str
	 *            a string to be parsed.
	 * @param delim
	 *            the delimiters.
	 */
	public StringTokenizer(String str, String delim) {
		this(str, delim, false);
	}

	/**
	 * Constructs a string tokenizer for the specified string. All characters in
	 * the <code>delim</code> argument are the delimiters for separating tokens.
	 * <p>
	 * If the <code>returnDelims</code> flag is <code>true</code>, then the
	 * delimiter characters are also returned as tokens. Each delimiter is
	 * returned as a string of length one. If the flag is <code>false</code>,
	 * the delimiter characters are skipped and only serve as separators between
	 * tokens.
	 * <p>
	 * Note that if <tt>delim</tt> is <tt>null</tt>, this constructor does not
	 * throw an exception. However, trying to invoke other methods on the
	 * resulting <tt>StringTokenizer</tt> may result in a
	 * <tt>NullPointerException</tt>.
	 * 
	 * @param str
	 *            a string to be parsed.
	 * @param delim
	 *            the delimiters.
	 * @param returnDelims
	 *            flag indicating whether to return the delimiters as tokens.
	 */
	public StringTokenizer(String str, String delim, boolean returnDelims) {
		this.currentPosition = 0;
		this.newPosition = -1;
		this.delimsChanged = false;
		this.str = str;
		this.maxPosition = str.length();
		this.delimiters = delim;
		this.retDelims = returnDelims;
		setMaxDelimChar();
	}

	/**
	 * Calculates the number of times that this tokenizer's
	 * <code>nextToken</code> method can be called before it generates an
	 * exception. The current position is not advanced.
	 * 
	 * @return the number of tokens remaining in the string using the current
	 *         delimiter set.
	 * @see StringTokenizer#nextToken()
	 */
	public int countTokens() {
		int count = 0;
		int currpos = this.currentPosition;
		while (currpos < this.maxPosition) {
			currpos = skipDelimiters(currpos);
			if (currpos >= this.maxPosition) {
				break;
			}
			currpos = scanToken(currpos);
			count++;
		}
		return count;
	}

	/**
	 * Returns the same value as the <code>hasMoreTokens</code> method. It
	 * exists so that this class can implement the <code>Enumeration</code>
	 * interface.
	 * 
	 * @return <code>true</code> if there are more tokens; <code>false</code>
	 *         otherwise.
	 * @see java.util.Enumeration
	 * @see StringTokenizer#hasMoreTokens()
	 */
	public boolean hasMoreElements() {
		return hasMoreTokens();
	}

	/**
	 * Tests if there are more tokens available from this tokenizer's string. If
	 * this method returns <tt>true</tt>, then a subsequent call to
	 * <tt>nextToken</tt> with no argument will successfully return a token.
	 * 
	 * @return <code>true</code> if and only if there is at least one token in
	 *         the string after the current position; <code>false</code>
	 *         otherwise.
	 */
	public boolean hasMoreTokens() {
		/*
		 * Temporary store this position and use it in the following nextToken()
		 * method only if the delimiters have'nt been changed in that
		 * nextToken() invocation.
		 */
		this.newPosition = skipDelimiters(this.currentPosition);
		return (this.newPosition < this.maxPosition);
	}

	/**
	 * Returns the same value as the <code>nextToken</code> method, except that
	 * its declared return value is <code>Object</code> rather than
	 * <code>String</code>. It exists so that this class can implement the
	 * <code>Enumeration</code> interface.
	 * 
	 * @return the next token in the string.
	 * @exception NoSuchElementException
	 *                if there are no more tokens in this tokenizer's string.
	 * @see java.util.Enumeration
	 * @see StringTokenizer#nextToken()
	 */
	public Object nextElement() {
		return nextToken();
	}

	/**
	 * Returns the next token from this string tokenizer.
	 * 
	 * @return the next token from this string tokenizer.
	 * @exception NoSuchElementException
	 *                if there are no more tokens in this tokenizer's string.
	 */
	public String nextToken() {
		/*
		 * If next position already computed in hasMoreElements() and delimiters
		 * have changed between the computation and this invocation, then use
		 * the computed value.
		 */

		this.currentPosition = (this.newPosition >= 0 && !this.delimsChanged) ? this.newPosition
				: skipDelimiters(this.currentPosition);

		/* Reset these anyway */
		this.delimsChanged = false;
		this.newPosition = -1;

		if (this.currentPosition >= this.maxPosition) {
			throw new NoSuchElementException();
		}
		int start = this.currentPosition;
		this.currentPosition = scanToken(this.currentPosition);
		return this.str.substring(start, this.currentPosition);
	}

	/**
	 * Returns the next token in this string tokenizer's string. First, the set
	 * of characters considered to be delimiters by this
	 * <tt>StringTokenizer</tt> object is changed to be the characters in the
	 * string <tt>delim</tt>. Then the next token in the string after the
	 * current position is returned. The current position is advanced beyond the
	 * recognized token. The new delimiter set remains the default after this
	 * call.
	 * 
	 * @param delim
	 *            the new delimiters.
	 * @return the next token, after switching to the new delimiter set.
	 * @exception NoSuchElementException
	 *                if there are no more tokens in this tokenizer's string.
	 */
	public String nextToken(String delim) {
		this.delimiters = delim;

		/* delimiter string specified, so set the appropriate flag. */
		this.delimsChanged = true;

		setMaxDelimChar();
		return nextToken();
	}

	/**
	 * Skips ahead from startPos and returns the index of the next delimiter
	 * character encountered, or maxPosition if no such delimiter is found.
	 */
	private int scanToken(int startPos) {
		int position = startPos;
		while (position < this.maxPosition) {
			char c = this.str.charAt(position);
			if ((c <= this.maxDelimChar) && (this.delimiters.indexOf(c) >= 0)) {
				break;
			}
			position++;
		}
		if (this.retDelims && (startPos == position)) {
			char c = this.str.charAt(position);
			if ((c <= this.maxDelimChar) && (this.delimiters.indexOf(c) >= 0)) {
				position++;
			}
		}
		return position;
	}

	/**
	 * Set maxDelimChar to the highest char in the delimiter set.
	 */
	private void setMaxDelimChar() {
		if (this.delimiters == null) {
			this.maxDelimChar = 0;
			return;
		}

		char m = 0;
		for (int i = 0; i < this.delimiters.length(); i++) {
			char c = this.delimiters.charAt(i);
			if (m < c) {
				m = c;
			}
		}
		this.maxDelimChar = m;
	}

	/**
	 * Skips delimiters starting from the specified position. If retDelims is
	 * false, returns the index of the first non-delimiter character at or after
	 * startPos. If retDelims is true, startPos is returned.
	 */
	private int skipDelimiters(int startPos) {
		if (this.delimiters == null) {
			throw new NullPointerException();
		}

		int position = startPos;
		while (!this.retDelims && position < this.maxPosition) {
			char c = this.str.charAt(position);
			if ((c > this.maxDelimChar) || (this.delimiters.indexOf(c) < 0)) {
				break;
			}
			position++;
		}
		return position;
	}
}
