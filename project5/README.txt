This program is conducted by Xiongfeng Hu Alone.
File Description:
1.	First, map each line to two lines, the first is  userX and the second line is the users followed by userX. And create a new Rdd - splitRdd to store the results.
	Example:
	userX:
	user1, user3, … , userK
2.	Second, we filter current Rdd to remove lines with symbol ‘:’ and create a new Rdd - finalRdd to store the results.
3.	Then, compute the frequency of each word in finalRdd and generates (word, frequency) pairs as the output.
4.	Finally, filter out lines with count > 1000 and store the results in largerThanOneThousand.
5.	When saveAsTextFile("output") is called, the program creates a new subdirectory named output, where part-xxxxx file(s) are generated that contain the (word, frequency) output pairs in largerThanOneThousand

