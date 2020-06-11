addi $t1, $t3, 8
jal label
ori $t7, $t7, 1
slti $t7, $t7, 0
j exit
slti $t7, $t7, 0
label:
andi $t7, $t7, 0
andi $t7, $t7, 0
jalr $s1, $ra

exit:
