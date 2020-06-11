foo:
    addi $a0, $a0, 17
    addi $sp, $sp, -32
    sb $a0, 16($sp)
    addi $a0, $a0, 1
    sh $a0, 12($sp)
    addi $a0, $a0, 1
    sw $a0, 8($sp)
    lw $a1, 8($sp)
    lh $a2, 12($sp)
    lb $a3, 16($sp)
    addiu $sp, $sp, 32
exit: