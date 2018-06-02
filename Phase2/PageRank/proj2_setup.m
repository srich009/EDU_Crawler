Info = csvread('pageRankInfo.csv');
[numdocs,col] = size(Info);
M = zeros(numdocs);
R = ones(numdocs,1);
d = 0.7;

%M: row j is all incoming links to doc j
%   col i is all outgoing links of doc i

for i = 1:numdocs 
    doc_i = Info(i,1);
    num_i = Info(i,2);
    for j = 3:col
        doc_j = Info(i,j);
        if (doc_j == 0)
            break
        end
        
        if (M(doc_j, doc_i) == 0)
            M(doc_j, doc_i) = 1 / num_i;
        else
            M(doc_j, doc_i) = M(doc_j, doc_i) + (1 / num_i);
        end
    end
end

M = M .* d;

b = ones(numdocs,1);
b = b .* ((1-d)/numdocs);

R_final = Pagerank(M,b,R);

docs = transpose(1:numdocs);
output = [docs,R_final];

csvwrite('finalPageRanks.csv',output);
