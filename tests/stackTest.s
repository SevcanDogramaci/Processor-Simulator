foo:
    addi $sp, $sp, -32
    addi $a0, $a0, -4
    sw $a0, 16($sp)
    sw $ra, 12($sp)
    sw $fp, 8($sp)
    addiu $fp, $sp, 28
    addi $a0, $a0, 1
    jal bar
    lw $fp, 8($sp)
    lw $ra, 12($sp)
    lw $a0, 16($sp)
    addiu $sp, $sp, 32
    j exit

bar: 
    addi $sp, $sp, -32
    sw $a0, 16($sp)
    sw $ra, 12($sp)
    sw $fp, 8($sp)
    addiu $fp, $sp, 28
    addi $v0, $a0, 1
    lw $fp, 8($sp)
    lw $ra, 12($sp)
    lw $a0, 16($sp)
    addi $sp, $sp, 32
    jr $ra

exit: