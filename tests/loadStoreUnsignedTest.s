foo:
    addi $sp, $sp, -32
    sw $a0, 16($sp)
    sw $ra, 12($sp)
    sw $fp, 8($sp)
    addiu $fp, $sp, 28
    addi $a0, $a0, 128
    sb $a0, 16($sp)
    lbu $a1, 16($sp)
    lhu $a2, 16($sp)