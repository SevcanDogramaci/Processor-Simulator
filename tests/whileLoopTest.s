addi $s2, $s2, 4
addi $t5, $t5, 16
sw $s2, 0($t5)
addi $s1, $s1, 1
j Cond				        # goto Cond
Loop:
    add $s0, $s0, $s1		# i = i + j
Cond:
    add $t0, $s0, $s0		# $t0 = 2 * i
	add $t0, $t0, $t0		# $t0 = 4 * i
	add $t1, $t0, $s3		# $t1 = &(A[i])
	lw $t2, 0($t1)			# $t2 = A[i]
	bne $t2, $s2, Loop		# goto Loop if ==
Exit: