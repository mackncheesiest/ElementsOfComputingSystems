// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.
// Use memory location 0 to store current screen address
(START)
	@16384
	D=A
	@0
	M=D
(POLLKEY)
	@24576
	D=M
	@KEYPRESSED
	D;JNE
(KEYNOTPRESSED)
	@0
	A=M
	M=0
	@0
	D=M
	@24576
	D=D-A
	@START
	D;JGE
	@0
	M=M+1
	@POLLKEY
	0;JMP
(KEYPRESSED)
	@0
	A=M
	M=-1
	@0
	D=M
	@24576
	D=D-A
	@START
	D;JGE
	@0
	M=M+1
	@POLLKEY
	0;JMP
	

	