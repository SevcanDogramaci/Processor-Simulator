addi $s1, $s1, 5        
addi $s2, $s2, 6
addi $s3, $s3, 0

beq $s3, $s4, true          # if $s3 == $s4 goto true
sub $s0, $s1, $s2           # else $s0 = $s1 - $s2
j exit
true: add $s0, $s1, $s2     # $s0 = $s1 + $s2
exit: 