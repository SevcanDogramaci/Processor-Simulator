addi $t0, $t0, 1
addi $a0, $a0, 2
sll $a1, $a0, 1
srl $a2, $a1, 1
sra $a3, $a2, 1

srlv $a1, $a1, $t0
sllv $a2, $a2, $t0
srav $a3, $a2, $t0