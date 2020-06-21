slti $r1, $r3, 8
muli $r3, $r1, 8

jal label
or $r2, $r1, $r3
muli $r1, $r2, -90
muli $r3, $r1, -45
slti $r1, $r1, 0
j exit

label:
and $r1, $r1, 0
jr $ra

exit:
sw $r3, -2($sp)