jal label

slt $r1, $r0, $sp
slti $r2, $r0, 300

sw $r1, 0($sp)
lw $r0, 0($sp)
sll $r1, $r1, $r1
srl $r2, $r1, $r1
lui $r3, 2
muli $r1, $r1, 5
mul $r1, $r1, $r2
bne $r0, $r1, exit

label: sub $sp, $sp, $r0
jr $ra

exit:
syscall $sp