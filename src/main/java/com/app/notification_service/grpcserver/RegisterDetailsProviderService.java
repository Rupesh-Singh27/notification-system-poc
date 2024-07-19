
package com.app.notification_service.grpcserver;

import com.app.grpc.RegistrationDetailsActionGrpc;
import com.app.grpc.RegistrationDetailsResponse;
import com.app.grpc.VoidRegistrationDetailsRequest;
import com.app.notification_service.model.RegistrationDetails;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@GrpcService
@Service
public class RegisterDetailsProviderService extends RegistrationDetailsActionGrpc.RegistrationDetailsActionImplBase {

    private final KafkaStreams createKTable;
    private ReadOnlyKeyValueStore<String, RegistrationDetails> registrationDetailsStore;
    public RegisterDetailsProviderService(KafkaStreams createKTable) {
        this.createKTable = createKTable;
    }

    @PostConstruct
    public void initializeStore(){
        registrationDetailsStore = createKTable.store(StoreQueryParameters
                .fromNameAndType("registration-details-state-store", QueryableStoreTypes.keyValueStore()));
    }

    @Override
    public void fetchRegistrationDetails(VoidRegistrationDetailsRequest request, StreamObserver<RegistrationDetailsResponse> responseObserver) {
        System.out.println("Call received from grpc client");
        List<com.app.grpc.RegistrationDetails> registrationDetailsList = new ArrayList<>();

        registrationDetailsStore.all().forEachRemaining((registrationDetailsKeyValue)-> {
            System.out.println("IKey: "+ registrationDetailsKeyValue.key + " IValue: " + registrationDetailsKeyValue.value);

            RegistrationDetails details = registrationDetailsKeyValue.value;

            com.app.grpc.RegistrationDetails response = com.app.grpc.RegistrationDetails
                    .newBuilder()
                    .setFirstName(details.getFirstName())
                    .setLastName(details.getLastName())
                    .setEmailId(details.getEmailId())
                    .setIsAdmin(details.isAdmin())
                    .build();

            registrationDetailsList.add(response);
        });

        if (!registrationDetailsList.isEmpty()) {
            responseObserver.onNext(RegistrationDetailsResponse.newBuilder().addAllAllRegistrationRequest(registrationDetailsList).build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(new Throwable("No New Records Available."));
        }
    }
}
