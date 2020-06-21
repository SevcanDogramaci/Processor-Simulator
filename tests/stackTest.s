foo:
    sw $a0, -17($sp)
    sw $ra, -21($sp)
    sw $sp, -25($sp)
    syscall $sp
    jal bar
    lw $sp, -25($sp)
    lw $ra, -21($sp)
    lw $a0, -17($sp)
    syscall $sp
    j exit

bar: 
    muli $a0, $a0, 10
    jr $ra

exit: