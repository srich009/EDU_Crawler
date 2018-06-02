
function R_curr = Pagerank(M,b,R_curr) 
eps = 1; %initialize eps variable
cnt = 0; %count iterations
while (eps > 0.001) %threshold to stop
    R_prev = R_curr; %copy over
    %does one iteration of pagerank
    R_curr = (M * R_prev) + b;
    %find difference between iterations
    R_dif = R_curr - R_prev;
    eps = norm(R_dif); %calc difference
    cnt = cnt + 1; 
    cnt %output to find number iterations
end 

return