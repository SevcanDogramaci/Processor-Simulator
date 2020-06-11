#for(int i = 0; i < 4; i++)
#    j += 5;

add $t0, $zero, $zero   # t0 = 0
j cond                  # goto loop condition
loop:
    addi $s0, $s0, 5
    addi $t0, $t0, 1
cond:
    slti $t1, $t0, 4
    bne $t1, $zero, loop
