
foo: sw $a0, -16($sp)
sw $ra, -20($sp)
sw $sp, -24($sp)
syscall $sp
jal bar
lw $sp, -24($sp)
lw $ra, -20($sp)
lw $a0, -16($sp)
syscall $sp
j exit
bar: lui $a0, 16
muli $a0, $a0, 10
jr $ra
exit: 