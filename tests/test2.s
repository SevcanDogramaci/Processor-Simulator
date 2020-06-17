jal label
label: sub $sp, $sp, $r0
jr $ra
sw $r0, 0($sp)
lw $r1, 0($sp)
sll $r1, $r1, $r1
srl $r1, $r1, $r1
lui $r3, 2
muli $r1, $r1, 0
mul $r1, $r1, $r2
bne $r0, $r1, label