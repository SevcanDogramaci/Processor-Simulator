slti $r1, $r1, 1
srl $sp, $r1, $sp
muli $r0, $sp, -2
sw $r0, 4($sp)
sll $sp, $sp, 1
