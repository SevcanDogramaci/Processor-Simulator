slti $r0, $r0, 5
muli $r1, $r0, 5
muli $r2, $r0, 6

beq $r3, $r3, true          # if $r3 == $r2 goto true
sub $r0, $r1, $r2           # else $r0 = $r1 - $r2
j exit
true: add $r0, $r1, $r2     # $r0 = $r1 + $r2
exit:
syscall $r0