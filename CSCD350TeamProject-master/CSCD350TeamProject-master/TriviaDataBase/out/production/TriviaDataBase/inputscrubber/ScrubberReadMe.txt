How to use this section:

Scrubber contains four methods, charScrubber, boolScrubber, shortScrubber, and intScrubber.

charScrubber: Takes in any string, including a null or empty string, and returns the appropriate a-b-c-d char.
If the string has a length != 1, the scrubber requests new input from the user.

boolScrubber: Takes in any string, including a null or empty string, and returns the appropriate boolean.
If the string is not true, false, t, or f (ignoring case), the scrubber asks for new input.

shortScrubber: Takes in any string, including a null or empty string, and returns a String array.
If the string contains more than two tokens or is null, the scrubber asks the user for new input.

intScrubber: Takes in any string, including a null or empty string, and returns the appropriate int.
If the string does not naturally contain an int, then the scrubber will ask the user for new input.