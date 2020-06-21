
add $r0, $r0, $sp # r0 = 255
add $r1, $r1, $sp # r1 = 255
muli $r0, $r0, 8 # r1 = 255*8
j cond                  # goto loop condition
loop:
    muli $r1, $r1, 2
cond:
    slt $r2, $r1, $r0
    bne $r2, $r3, loop
